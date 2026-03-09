package com.project.shiphub.service.report;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.project.shiphub.repository.report.ReportRepository;
import lombok.Data;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Data
public class ReportService {

    private final ReportRepository reportRepository;

    public byte[] generateCommissionReport(LocalDate startDate, LocalDate endDate) throws Exception {

       List<Object[]> resultados = reportRepository.CommissionReport(startDate, endDate);

        String html = buildHtml(resultados, startDate, endDate);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfRendererBuilder builder = new PdfRendererBuilder();
        builder.withHtmlContent(html, null);
        builder.toStream(outputStream);
        builder.run();

        return outputStream.toByteArray();
    }

    private String buildHtml(List<Object[]> resultados, LocalDate dataInicio, LocalDate dataFim) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        StringBuilder rows = new StringBuilder();


        for (Object[] row : resultados) {
            System.out.println("Data: " + row[6]);
            rows.append("<tr>")
                    .append("<td class='store-name'>").append(row[0]).append("</td>")
                    .append("<td class='product-name'>").append(row[1]).append("</td>")
                    .append("<td class='amount total'>R$ ").append(row[2]).append("</td>")
                    .append("<td class='amount discount'>R$ ").append(row[3]).append("</td>")
                    .append("<td class='amount commission'>R$ ").append(row[4]).append("</td>")
                    .append("<td class='amount profit'>R$ ").append(row[5]).append("</td>")
                    .append("<td class='date-cell'>").append(LocalDateTime.parse(row[6].toString().replace("T", " "), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")).format(fmt)).append("</td>")
                    .append("</tr>");
        }

        return """
    <!DOCTYPE html>
    <html>
    <head>
    <meta charset="UTF-8"/>
    <style>
      * { margin: 0; padding: 0; box-sizing: border-box; }
                      body { font-family: 'Helvetica Neue', Arial, sans-serif; background: #f4f6f9; padding: 40px; color: #1a1a2e; }
                
                      .header { background: #1565C0; border-radius: 16px; padding: 40px; margin-bottom: 32px; color: white; }
                      .header-top { display: block; }
                      .badge { float: right; background: #1976D2; border: 1px solid #90CAF9; border-radius: 8px; padding: 8px 16px; font-size: 12px; color: white; }
                      .report-period { font-size: 13px; color: #BBDEFB; margin-top: 4px; }
                      .report-title { font-size: 26px; font-weight: 800; color: white; letter-spacing: -0.5px; }
                      .badge { background: rgba(255,255,255,0.15); border: 1px solid rgba(255,255,255,0.3); border-radius: 8px; padding: 8px 16px; font-size: 12px; color: white; }
                
                      .table-wrapper { background: white; border-radius: 16px; border: 1px solid #e8ecf0; overflow: hidden; }
                      .table-header { padding: 20px 24px; border-bottom: 1px solid #f1f5f9; }
                      .table-title { font-size: 15px; font-weight: 700; color: #1565C0; }
                
                      table { width: 100%%; border-collapse: collapse; }
                      thead tr { background: #E3F2FD; }
                      th { padding: 12px 16px; text-align: left; font-size: 10px; font-weight: 700; color: #1565C0; text-transform: uppercase; letter-spacing: 1px; border-bottom: 2px solid #BBDEFB; }
                      td { padding: 14px 16px; font-size: 12px; color: #334155; border-bottom: 1px solid #f8fafc; white-space: nowrap; }
                      tr:last-child td { border-bottom: none; }
                      tr:hover td { background: #F5F9FF; }
                
                      .store-name { font-weight: 700; color: #1565C0; }
                      .product-name { color: #64748b; }
                      .amount { font-weight: 700; font-family: monospace; }
                      .amount.total { color: #1a1a2e; }
                      .amount.discount { color: #EF5350; }
                      .amount.commission { color: #1565C0; }
                      .amount.profit { color: #2E7D32; }
                      .date-cell { color: #94a3b8; font-size: 11px; white-space: nowrap; }
                
                      .footer { margin-top: 24px; text-align: center; font-size: 11px; color: #94a3b8; }
    </style>
    </head>
    <body>
                            <div class="header">
                                <div class="header-top">
                                    <div>
                                        <p class="brand">GoDrop</p>
                                        <h1 class="report-title">Relatório de Comissões</h1>
                                        <p class="report-period">Período: %s até %s</p>
                                    </div>
                                    <div class="badge">Gerado em %s</div>
                                </div>
                            </div>
                
                            <div class="table-wrapper">
                                <div class="table-header">
                                    <p class="table-title">Detalhes das Transações</p>
                                </div>
                                <table>
                                    <thead>
                                        <tr>
                                            <th>Loja</th>
                                            <th>Produto</th>
                                            <th>Total da Venda</th>
                                            <th>Desconto</th>
                                            <th>Comissão da Loja</th>
                                            <th>Meu Lucro</th>
                                            <th>Data</th>
                                        </tr>
                                    </thead>
                                    <tbody>%s</tbody>
                                </table>
                            </div>
                
                            <div class="footer">Plataforma GoDrop · Relatório Confidencial · Gerado automaticamente</div>
                        </body>
                        </html>
""".formatted(dataInicio.format(fmt), dataFim.format(fmt), LocalDate.now().format(fmt), rows.toString());
    }

    public byte[] generateTopProductsReport(LocalDate startDate, LocalDate endDate) throws Exception {
        List<Object[]> resultados = reportRepository.topProductsReport(startDate, endDate);
        String html = buildTopProductsHtml(resultados, startDate, endDate);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfRendererBuilder builder = new PdfRendererBuilder();
        builder.withHtmlContent(html, null);
        builder.toStream(outputStream);
        builder.run();
        return outputStream.toByteArray();
    }

    private String buildTopProductsHtml(List<Object[]> resultados, LocalDate dataInicio, LocalDate dataFim) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        StringBuilder rows = new StringBuilder();

        for (Object[] row : resultados) {
            rows.append("<tr>")
                    .append("<td class='product-name'>").append(row[0]).append("</td>")
                    .append("<td class='amount total'>").append(row[1]).append("</td>")
                    .append("<td class='amount profit'>R$ ").append(row[2]).append("</td>")
                    .append("<td class='amount discount'>R$ ").append(row[3]).append("</td>")
                    .append("</tr>");
        }

        return """
        <!DOCTYPE html>
        <html>
        <head>
        <meta charset="UTF-8"/>
        <style>
            * { margin: 0; padding: 0; box-sizing: border-box; }
            body { font-family: 'Helvetica Neue', Arial, sans-serif; background: #f4f6f9; padding: 40px; color: #1a1a2e; }
            .header { background: #1565C0; border-radius: 16px; padding: 40px; margin-bottom: 32px; color: white; }
            .header-top { display: flex; justify-content: space-between; align-items: flex-start; }
            .brand { font-size: 13px; font-weight: 700; letter-spacing: 3px; text-transform: uppercase; color: #90CAF9; margin-bottom: 8px; }
            .report-title { font-size: 26px; font-weight: 800; color: white; letter-spacing: -0.5px; }
            .report-period { font-size: 13px; color: rgba(255,255,255,0.7); margin-top: 4px; }
            .badge { background: rgba(255,255,255,0.15); border: 1px solid rgba(255,255,255,0.3); border-radius: 8px; padding: 8px 16px; font-size: 12px; color: white; }
            .table-wrapper { background: white; border-radius: 16px; border: 1px solid #e8ecf0; overflow: hidden; }
            .table-header { padding: 20px 24px; border-bottom: 1px solid #f1f5f9; }
            .table-title { font-size: 15px; font-weight: 700; color: #1565C0; }
            table { width: 100%%; border-collapse: collapse; }
            thead tr { background: #E3F2FD; }
            th { padding: 12px 16px; text-align: left; font-size: 10px; font-weight: 700; color: #1565C0; text-transform: uppercase; letter-spacing: 1px; border-bottom: 2px solid #BBDEFB; }
            td { padding: 14px 16px; font-size: 12px; color: #334155; border-bottom: 1px solid #f8fafc; white-space: nowrap; }
            tr:last-child td { border-bottom: none; }
            tr:hover td { background: #F5F9FF; }
            .product-name { font-weight: 700; color: #1565C0; }
            .amount { font-weight: 700; font-family: monospace; }
            .amount.total { color: #1a1a2e; }
            .amount.discount { color: #EF5350; }
            .amount.profit { color: #2E7D32; }
            .footer { margin-top: 24px; text-align: center; font-size: 11px; color: #94a3b8; }
        </style>
        </head>
        <body>
            <div class="header">
                <div class="header-top">
                    <div>
                        <p class="brand">GoDrop</p>
                        <h1 class="report-title">Produtos Mais Vendidos</h1>
                        <p class="report-period">Período: %s até %s</p>
                    </div>
                    <div class="badge">Gerado em %s</div>
                </div>
            </div>
            <div class="table-wrapper">
                <div class="table-header">
                    <p class="table-title">Ranking de Produtos</p>
                </div>
                <table>
                    <thead>
                        <tr>
                            <th>Produto</th>
                            <th>Qtd. Vendida</th>
                            <th>Receita Total</th>
                            <th>Descontos</th>
                        </tr>
                    </thead>
                    <tbody>%s</tbody>
                </table>
            </div>
            <div class="footer">Plataforma GoDrop · Relatório Confidencial · Gerado automaticamente</div>
        </body>
        </html>
    """.formatted(dataInicio.format(fmt), dataFim.format(fmt), LocalDate.now().format(fmt), rows.toString());
    }

    public byte[] generateCriticalStockReport() throws Exception {
        List<Object[]> resultados = reportRepository.criticalStockReport();
        String html = buildCriticalStockHtml(resultados);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfRendererBuilder builder = new PdfRendererBuilder();
        builder.withHtmlContent(html, null);
        builder.toStream(outputStream);
        builder.run();
        return outputStream.toByteArray();
    }

    private String buildCriticalStockHtml(List<Object[]> resultados) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        StringBuilder rows = new StringBuilder();

        for (Object[] row : resultados) {
            int estoque = ((Number) row[1]).intValue();
            String estoqueClass = estoque <= 5 ? "amount discount" : "amount total";
            rows.append("<tr>")
                    .append("<td class='product-name'>").append(row[0]).append("</td>")
                    .append("<td class='").append(estoqueClass).append("'>").append(estoque).append(" un.</td>")
                    .append("<td>").append(row[2] != null ? row[2] : "-").append("</td>")
                    .append("<td>").append((Boolean) row[3] ? "Ativo" : "Inativo").append("</td>")
                    .append("</tr>");
        }

        return """
        <!DOCTYPE html>
        <html>
        <head>
        <meta charset="UTF-8"/>
        <style>
            * { margin: 0; padding: 0; box-sizing: border-box; }
            body { font-family: 'Helvetica Neue', Arial, sans-serif; background: #f4f6f9; padding: 40px; color: #1a1a2e; }
            .header { background: #1565C0; border-radius: 16px; padding: 40px; margin-bottom: 32px; color: white; }
            .header-top { display: block; }
            .brand { font-size: 13px; font-weight: 700; letter-spacing: 3px; text-transform: uppercase; color: #90CAF9; margin-bottom: 8px; }
            .report-title { font-size: 26px; font-weight: 800; color: white; letter-spacing: -0.5px; }
            .report-period { font-size: 13px; color: #BBDEFB; margin-top: 4px; }
            .badge { float: right; background: #1976D2; border: 1px solid #90CAF9; border-radius: 8px; padding: 8px 16px; font-size: 12px; color: white; }
            .table-wrapper { background: white; border-radius: 16px; border: 1px solid #e8ecf0; overflow: hidden; }
            .table-header { padding: 20px 24px; border-bottom: 1px solid #f1f5f9; }
            .table-title { font-size: 15px; font-weight: 700; color: #1565C0; }
            table { width: 100%%; border-collapse: collapse; }
            thead tr { background: #E3F2FD; }
            th { padding: 12px 16px; text-align: left; font-size: 10px; font-weight: 700; color: #1565C0; text-transform: uppercase; letter-spacing: 1px; border-bottom: 2px solid #BBDEFB; }
            td { padding: 14px 16px; font-size: 12px; color: #334155; border-bottom: 1px solid #f8fafc; white-space: nowrap; }
            tr:last-child td { border-bottom: none; }
            tr:hover td { background: #F5F9FF; }
            .product-name { font-weight: 700; color: #1565C0; }
            .amount { font-weight: 700; font-family: monospace; }
            .amount.total { color: #1a1a2e; }
            .amount.discount { color: #EF5350; }
            .amount.profit { color: #2E7D32; }
            .footer { margin-top: 24px; text-align: center; font-size: 11px; color: #94a3b8; }
        </style>
        </head>
        <body>
            <div class="header">
                <div class="header-top">
                    <div class="badge">Gerado em %s</div>
                    <p class="brand">GoDrop</p>
                    <h1 class="report-title">Estoque Crítico</h1>
                    <p class="report-period">Produtos com menos de 10 unidades</p>
                </div>
            </div>
            <div class="table-wrapper">
                <div class="table-header">
                    <p class="table-title">Produtos que precisam de reposição</p>
                </div>
                <table>
                    <thead>
                        <tr>
                            <th>Produto</th>
                            <th>Estoque</th>
                            <th>Categoria</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>%s</tbody>
                </table>
            </div>
            <div class="footer">Plataforma GoDrop · Relatório Confidencial · Gerado automaticamente</div>
        </body>
        </html>
    """.formatted(LocalDate.now().format(fmt), rows.toString());
    }
}
