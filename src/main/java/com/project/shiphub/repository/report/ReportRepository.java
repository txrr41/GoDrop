package com.project.shiphub.repository.report;

import com.project.shiphub.model.product.Product;
import com.project.shiphub.repository.payment.DropperCommissionRepository;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.parser.Entity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class ReportRepository  {

    private final EntityManager entityManager;


    public ReportRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<Object[]> CommissionReport(LocalDate startDate, LocalDate endDate) {
        jakarta.persistence.Query query = entityManager.createNativeQuery("""  
            SELECT     
                ds.store_name,
                p.nome AS produto,
                dc.total_amount,
                dc.platform_fee AS desconto,
                dc.dropper_margin AS comissao_loja_parceira,
                (dc.total_amount - dc.dropper_margin - dc.platform_fee) AS lucro_total, 
                dc.created_at
            FROM dropper_commission dc
            JOIN orders o ON o.id = dc.order_id
            JOIN order_items oi ON oi.order_id = o.id
            JOIN products p ON p.id = oi.product_id
            JOIN store_products sp ON sp.product_id = p.id
            JOIN dropper_stores ds ON ds.id = sp.store_id
            WHERE dc.status = 'PAID'
            AND dc.created_at BETWEEN :startDate AND :endDate
            ORDER BY ds.store_name
            """);
        query.setParameter("startDate", startDate.atStartOfDay());
        query.setParameter("endDate", endDate.atTime(23, 59, 59));
         return (List<Object[]>) query.getResultList();
    }

    public List<Object[]> topProductsReport(LocalDate startDate, LocalDate endDate) {
        jakarta.persistence.Query query = entityManager.createNativeQuery("""
        SELECT
            p.nome,
            SUM(oi.quantity) AS quantidade,
            SUM(dc.total_amount) AS receita,
            SUM(dc.platform_fee) AS descontos
        FROM order_items oi
        JOIN products p ON p.id = oi.product_id
        JOIN orders o ON o.id = oi.order_id
        JOIN dropper_commission dc ON dc.order_id = o.id
        WHERE dc.status = 'PAID'
        AND dc.created_at BETWEEN :startDate AND :endDate
        GROUP BY p.nome
        ORDER BY quantidade DESC
        """);
        query.setParameter("startDate", startDate.atStartOfDay());
        query.setParameter("endDate", endDate.atTime(23, 59, 59));
        return (List<Object[]>) query.getResultList();
    }

    public List<Object[]> criticalStockReport() {
        jakarta.persistence.Query query = entityManager.createNativeQuery("""
        SELECT
            p.nome,
            p.estoque,
            p.categoria,
            p.ativo
        FROM products p
        WHERE p.estoque <= 10
        AND p.ativo = true
        ORDER BY p.estoque ASC
        """);
        return (List<Object[]>) query.getResultList();
    }

}

